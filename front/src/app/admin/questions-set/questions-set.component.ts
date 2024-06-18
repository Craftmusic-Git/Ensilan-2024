import {Component, inject} from '@angular/core';
import {RouterLink} from "@angular/router";
import {QuestionSetAdminService} from '@app/core/service/question-set-admin.service';
import {QuestionSetStore} from '@app/core/store/question-set.store';
import {QuestionSetDto} from "@domain/questions/question-set.dto";

@Component({
  selector: 'app-questions-set',
  standalone: true,
  imports: [
    RouterLink,
  ],
  templateUrl: './questions-set.component.html',
  styleUrl: './questions-set.component.scss',
})
export class QuestionsSetComponent {
  questionSetService = inject(QuestionSetAdminService);
  questionSetStore = inject(QuestionSetStore);

  constructor(){
    this.questionSetStore.refresh();
  }

  deleteQuestionSet( id: string ){
    this.questionSetService.delete(id).subscribe(() => this.questionSetStore.refresh());
  }

  createQuestionSet(){
    this.questionSetService.create(Object.assign(new QuestionSetDto(), {name: 'New question set'}))
      .subscribe(() => this.questionSetStore.refresh());
  }
}
