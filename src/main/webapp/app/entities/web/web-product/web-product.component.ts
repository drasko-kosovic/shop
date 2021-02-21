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
  searchMode: boolean | undefined;

  constructor(protected productService: WebProductService, protected route: ActivatedRoute) {}

  ngOnInit(): void {
    // const id = this.route.snapshot.paramMap.get('id');
    this.route.queryParams.subscribe(queryParams => {
      // do something with the query params
    });
    this.route.params.subscribe(routeParams => {
      this.listProducts();
    });

    // this.route.queryParams.subscribe(queryParams => {
    //   // do something with the query params
    // });
    // this.route.params.subscribe(routeParams => {
    //   this.loadPageProductName();
    // });
  }

  loadPageCategoryId(): void {
    this.productService.queryId(this.route.snapshot.paramMap.get('id')).subscribe(res => {
      this.products = res;

      // eslint-disable-next-line no-console
      // console.log('to je   ' + res);
    });
  }

  loadPageProductName(): void {
    const theKeyword: any = this.route.snapshot.paramMap.get('name');
    this.productService.queryName(theKeyword).subscribe(res => {
      this.products = res;

      // eslint-disable-next-line no-console
      // console.log('to je   ' + res);
    });
  }

  listProducts(): void {
    this.searchMode = this.route.snapshot.paramMap.has('name');

    if (this.searchMode) {
      this.loadPageProductName();
    } else {
      this.loadPageCategoryId();
    }
  }
}
