import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WebPageComponent } from './web-page/web-page.component';
import { WebProductComponent } from './web-product/web-product.component';

const routes: Routes = [
  {
    path: '',
    component: WebPageComponent,
    children: [
      {
        path: 'web-product/:id',
        component: WebProductComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class WebRoutingModule {}
