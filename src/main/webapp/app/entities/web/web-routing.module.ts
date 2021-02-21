import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WebPageComponent } from './web-page/web-page.component';
import { WebProductComponent } from './web-product/web-product.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';

const routes: Routes = [
  {
    path: '',
    component: WebPageComponent,
    children: [
      {
        path: 'web-product/:id',
        component: WebProductComponent,
      },
      {
        path: 'search/:name',
        component: WebProductComponent,
      },
    ],
  },
  {
    path: 'web-product',
    component: WebProductComponent,
  },
  {
    path: 'web-product/web-product-detail/:id',
    component: ProductDetailComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class WebRoutingModule {}
