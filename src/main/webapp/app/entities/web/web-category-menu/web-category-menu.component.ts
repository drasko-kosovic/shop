import { Component, OnInit } from '@angular/core';
import { IProductCategory } from '../../../shared/model/product-category.model';
import { WebCategoryMenuService } from './web-category-menu.service';

@Component({
  selector: 'jhi-web-category-menu',
  templateUrl: './web-category-menu.component.html',
  styleUrls: ['./web-category-menu.component.scss'],
})
export class WebCategoryMenuComponent implements OnInit {
  productsCategory?: IProductCategory[];

  constructor(protected categoryService: WebCategoryMenuService) {}

  ngOnInit(): void {
    this.loadPage();
  }

  loadPage(): void {
    this.categoryService.queryCategory().subscribe(res => {
      this.productsCategory = res;
      // eslint-disable-next-line no-console
      console.log('to je   ' + res);
    });
  }
}
