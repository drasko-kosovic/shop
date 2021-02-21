import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { WebRoutingModule } from './web-routing.module';
import { WebPageComponent } from './web-page/web-page.component';
import { WebCategoryMenuComponent } from './web-category-menu/web-category-menu.component';
import { WebProductComponent } from './web-product/web-product.component';
import { WebSearchComponent } from './web-search/web-search.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';

@NgModule({
  declarations: [WebPageComponent, WebCategoryMenuComponent, WebProductComponent, WebSearchComponent, ProductDetailComponent],
  imports: [CommonModule, WebRoutingModule],
})
export class WebModule {}
