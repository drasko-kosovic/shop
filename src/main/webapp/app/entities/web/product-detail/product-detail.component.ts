import { Component, OnInit } from '@angular/core';
import { Product } from '../../../shared/model/product.model';
import { ProductService } from '../../product/product.service';
import { ActivatedRoute } from '@angular/router';
import { WebProductService } from '../web-product/web-product.service';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit {
  product: Product = new Product();

  constructor(private productService: WebProductService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleProductDetails();
    });
  }

  handleProductDetails(): void {
    // get the "id" param string. convert string to a number using the "+" symbol
    // const theProductId: number = +this.route.snapshot.paramMap.get('id');

    this.productService.queryProductId(this.route.snapshot.paramMap.get('id')).subscribe(data => {
      this.product = data;
    });
  }
}
