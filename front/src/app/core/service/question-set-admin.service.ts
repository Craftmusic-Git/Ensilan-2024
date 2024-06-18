import {Injectable} from '@angular/core';
import {QuestionSetDto} from "@domain/questions/question-set.dto";
import {BaseService} from './base.service';

@Injectable({
  providedIn: 'root',
})
export class QuestionSetAdminService extends BaseService<QuestionSetDto> {

  constructor(){
    super('admin/questions-set');
  }
}
