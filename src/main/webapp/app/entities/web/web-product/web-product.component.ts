import { Component, OnInit } from '@angular/core';
import { IProduct } from '../../../shared/model/product.model';

import { ActivatedRoute } from '@angular/router';
import { WebProductService } from './web-product.service';

@Component({
  selector: 'jhi-web-product',
  templateUrl: './web-product.component.html',
  styleUrls: ['./web-product.component.scss'],
})
export class WebProductComponent implements OnInit {
  products?: IProduct[];
  constructor(protected productService: WebProductService, protected route: ActivatedRoute) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.route.queryParams.subscribe(queryParams => {
      // do something with the query params
    });
    this.route.params.subscribe(routeParams => {
      this.loadPageCategoryId();
    });
  }

  loadPageCategoryId(): void {
    this.productService.queryId(this.route.snapshot.paramMap.get('id')).subscribe(res => {
      this.products = res;

      // eslint-disable-next-line no-console
      // console.log('to je   ' + res);
    });
  }

  loadPageProductName(): void {
    this.productService.queryName(this.route.snapshot.paramMap.get('name')).subscribe(res => {
      this.products = res;

      // eslint-disable-next-line no-console
      // console.log('to je   ' + res);
    });
  }
}
