import {Injectable} from "@angular/core";
import {AbstractStore} from "@core/store/abstract.store";
import {QuestionSetDto} from "@domain/questions/question-set.dto";
import { QuestionSetAdminService } from "../service/question-set-admin.service";

@Injectable({providedIn: 'root'})
export class QuestionSetStore extends AbstractStore<QuestionSetDto> {
  constructor(questionSetService: QuestionSetAdminService){
    super(questionSetService);
  }
}
