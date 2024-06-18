import {Component, inject, OnInit, signal} from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {ActivatedRoute} from "@angular/router";
import {QuestionSetAdminService} from "@core/service/question-set-admin.service";
import {QuestionsAdminService} from "@core/service/questions-admin.service";
import {AnswerDto} from "@domain/questions/answer.dto";
import {QuestionSetDto} from "@domain/questions/question-set.dto";
import {QuestionDto} from "@domain/questions/question.dto";

@Component({
  selector: 'app-questions',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './questions.component.html',
  styleUrl: './questions.component.scss',
})
export class QuestionsComponent {
  questionSetService = inject(QuestionSetAdminService);
  questionService = inject(QuestionsAdminService);
  route = inject(ActivatedRoute);

  private fb = inject(FormBuilder);

  currentQuestionSet = signal<QuestionSetDto>(null);

  form = this.fb.group({
    id: '',
    name: ['', Validators.required],
    questions: this.fb.array([]),
  });

  get questions(){
    return this.form.get('questions') as FormArray;
  }

  getAnswers( index: number ){
    return this.questions.at(index).get('answers') as FormArray;
  }

  getQuestionFormGroup( index: number ){
    return this.questions.at(index) as FormGroup<any>;
  }

  getAnswerFormGroup( questionIndex: number, answerIndex: number ){
    return this.getAnswers(questionIndex).at(answerIndex) as FormGroup<any>;

  }

  constructor(){
    this.route.params.subscribe(params => {
      this.questionSetService.get(params['id']).subscribe(set => {
        this.refreshData(set);
      });
    });
  }

  addQuestion(){
    const question = this.fb.group({
      id: [''],
      text: ['', Validators.required],
      order: [this.questions.length + 1],
      answers: this.fb.array([]),
    });
    this.questions.push(question);
  }

  addAnswer( questionIndex: number ){
    const answer = this.fb.group({
      id: [''],
      text: ['', Validators.required],
      isCorrect: [false],
    });
    this.getAnswers(questionIndex).push(answer);
  }

  buildQuestionFormGroup( question: QuestionDto ): FormGroup<any>{

    return this.fb.group({
      id: [question.id],
      text: [question.text, Validators.required],
      order: [question.order],
      answers: this.fb.array([]),
    }) as FormGroup<any>;
  }

  buildAnswerFormGroup( answer: AnswerDto ): FormGroup<any>{
    return this.fb.group({
      id: [answer.id],
      text: [answer.text, Validators.required],
      isCorrect: [answer.isCorrect],
      answer: this.fb.array([]),
    }) as FormGroup;
  }

  deleteQuestion( index: number ){
    this.questions.removeAt(index);
    this.questions.controls.forEach(( control, i ) => {
      control.patchValue({order: i + 1});
    });
  }

  deleteAnswer( questionIndex: number, answerIndex: number ){
    this.getAnswers(questionIndex).removeAt(answerIndex);
  }

  refreshData( set: QuestionSetDto ){
    set.questions = set.questions.sort(( a, b ) => a.order - b.order);
    this.currentQuestionSet.set(set);
    this.questions.clear();

    const questionsGroup = set.questions.map(question => this.buildQuestionFormGroup(question));
    this.form.patchValue({
      id: set.id,
      name: set.name,
    });

    for ( let i = 0; i < questionsGroup.length; i++ ) {
      const question = set.questions[i];
      this.questions.push(this.buildQuestionFormGroup(question))
    }

    for ( let i = 0; i < questionsGroup.length; i++ ) {
      const answersGroup = set.questions[i].answers.map(answer => this.buildAnswerFormGroup(answer));
      for ( let j = 0; j < answersGroup.length; j++ ) {
        this.getAnswers(i).push(answersGroup[j]);
      }
    }
  }

  onSubmit(){
    const questionSet = Object.assign(new QuestionSetDto(), {
      id: this.form.value.id,
      name: this.form.value.name,
      questions: this.questions.value.map(( question: QuestionDto ) => {
        return {
          id: question.id,
          text: question.text,
          order: question.order,
          answers: question.answers.map(answer => {
            return {
              id: answer.id,
              text: answer.text,
              isCorrect: answer.isCorrect,
            };
          }),
        };
      }),
    } as QuestionSetDto);

    console.log(questionSet)

    this.questionSetService.update(questionSet.id, questionSet).subscribe(() => {
      this.questionSetService.get(questionSet.id).subscribe(set => {
        this.refreshData(set);
      });
    });
  }

  protected readonly FormGroup = FormGroup;
}
