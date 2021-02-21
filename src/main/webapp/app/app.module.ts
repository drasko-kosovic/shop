import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ShopSharedModule } from 'app/shared/shared.module';
import { ShopCoreModule } from 'app/core/core.module';
import { ShopAppRoutingModule } from './app-routing.module';
import { ShopHomeModule } from './home/home.module';
import { ShopEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { AngularComponent } from './angular/angular.component';

@NgModule({
  imports: [
    BrowserModule,
    ShopSharedModule,
    ShopCoreModule,
    ShopHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ShopEntityModule,
    ShopAppRoutingModule,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    AngularComponent,
  ],
  bootstrap: [MainComponent],
})
export class ShopAppModule {}
