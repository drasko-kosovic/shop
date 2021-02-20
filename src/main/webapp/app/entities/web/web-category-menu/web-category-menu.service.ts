import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../../../app.constants';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProductCategory } from '../../../shared/model/product-category.model';

@Injectable({
  providedIn: 'root',
})
export class WebCategoryMenuService {
  public resourceUrlCategories = SERVER_API_URL + 'api/product-categories';

  constructor(protected http: HttpClient) {}

  queryCategory(): Observable<IProductCategory[]> {
    return this.http.get<IProductCategory[]>(this.resourceUrlCategories);
  }
}
