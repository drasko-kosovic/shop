import { Component, OnInit } from '@angular/core';
import { IProduct } from '../../../shared/model/product.model';
import { WebProductService } from './web-product.service';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-web-product',
  templateUrl: './web-product.component.html',
  styleUrls: ['./web-product.component.scss'],
})
export class WebProductComponent implements OnInit {
  products?: IProduct[];
  sub?: Subscription;
  id?: number;

  constructor(protected productService: WebProductService, protected route: ActivatedRoute) {}

  ngOnInit(): void {
    // const id = this.route.snapshot.paramMap.get('id');

    // eslint-disable-next-line no-console
    // console.log(id);
    this.loadPage();
  }

  loadPage(): void {
    // check if "id" parameter is available

    this.productService
      .queryId(this.route.snapshot.paramMap.get('id'))
      // .queryId(1951)
      .subscribe(res => {
        this.products = res;
        // eslint-disable-next-line no-console
        console.log('to je   ' + res);
      });
  }
}
