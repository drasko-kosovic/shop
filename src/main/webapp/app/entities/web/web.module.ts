import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { WebRoutingModule } from './web-routing.module';
import { WebPageComponent } from './web-page/web-page.component';
import { WebCategoryMenuComponent } from './web-category-menu/web-category-menu.component';
import { WebProductComponent } from './web-product/web-product.component';

@NgModule({
  declarations: [WebPageComponent, WebCategoryMenuComponent, WebProductComponent],
  imports: [CommonModule, WebRoutingModule],
})
export class WebModule {}