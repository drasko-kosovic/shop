import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../../../app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IProduct } from '../../../shared/model/product.model';
import { Observable } from 'rxjs';

type EntityResponseType = HttpResponse<IProduct>;
type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({
  providedIn: 'root',
})
export class WebProductService {
  public resourceUrl = SERVER_API_URL + 'api/products';
  public resourceUrlProductCategoryId = SERVER_API_URL + 'api/products/category?ProductCategoryId=';
  constructor(protected http: HttpClient) {}

  queryId(id: any): Observable<IProduct[]> {
    return this.http.get<IProduct[]>(this.resourceUrlProductCategoryId + id);
  }

  query(): Observable<IProduct[]> {
    return this.http.get<IProduct[]>(this.resourceUrl);
  }
}
