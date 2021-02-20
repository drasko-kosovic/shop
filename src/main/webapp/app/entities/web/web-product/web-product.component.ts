import { Component, OnInit } from '@angular/core';
import { IProduct } from '../../../shared/model/product.model';
import { WebProductService } from './web-product.service';
import { ActivatedRoute } from '@angular/router';

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

    // eslint-disable-next-line no-console
    console.log('broj je -----------' + id);
    this.loadPage();
  }

  loadPage(): void {
    this.productService
      .queryId(this.route.snapshot.paramMap.get('id'))
      // .queryId(1951)
      .subscribe(res => {
        this.products = res;
        // eslint-disable-next-line no-console
        // console.log('to je   ' + res);
      });
  }
}
