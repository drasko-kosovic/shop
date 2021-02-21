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
  idMode: boolean | undefined;

  constructor(protected productService: WebProductService, protected route: ActivatedRoute) {}

  ngOnInit(): void {
    // const id = this.route.snapshot.paramMap.get('id');
    this.route.queryParams.subscribe(queryParams => {
      // do something with the query params
    });
    this.route.params.subscribe(routeParams => {
      this.listProducts();
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
    const theKeyword: any = this.route.snapshot.paramMap.get('name');
    this.productService.queryName(theKeyword).subscribe(res => {
      this.products = res;

      // eslint-disable-next-line no-console
      // console.log('to je   ' + res);
    });
  }

  loadPageAllProduct(): void {
    this.productService.query().subscribe(res => {
      this.products = res;
    });
  }

  listProducts(): void {
    this.searchMode = this.route.snapshot.paramMap.has('name');
    this.idMode = this.route.snapshot.paramMap.has('id');
    if (this.searchMode) {
      this.loadPageProductName();
    }
    if (this.idMode) {
      this.loadPageCategoryId();
    } else {
      this.loadPageAllProduct();
    }
  }
}
