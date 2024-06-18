import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { QuestionDto } from '@domain/questions/question.dto';

@Injectable({
  providedIn: 'root'
})
export class QuestionsAdminService extends BaseService<QuestionDto>{

  constructor() {
    super('admin/questions');
  }
}
