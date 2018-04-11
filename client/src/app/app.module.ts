import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';

import { AppRoutingModule } from './app-routing.module';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import {BackendService} from './services/backend.service';
import { RouterModule, Routes } from '@angular/router';

import { FilterPipe} from './filter.pipe';
import { ClickOutsideModule } from 'ng4-click-outside';
import { ProductPageModule } from './product-page/product-page.module';
import { SharedModule } from './shared/shared.module';
import { HomeModule } from './home/home.module';
import { GlobalService } from './global-service.service';
import { CartModule } from './cart/cart.module';
import { CustomerModule } from './customer/customer.module';
import { CheckoutModule } from './checkout/checkout.module';
import { VirtualScrollModule } from 'angular2-virtual-scroll';


@NgModule({
  imports: [
    AppRoutingModule,
    ProductPageModule,
    HomeModule,
    SharedModule,
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule,
    CartModule,
    ClickOutsideModule,
    CustomerModule,
    CheckoutModule,
    VirtualScrollModule,
  ],
  declarations: [AppComponent],
  providers: [BackendService, GlobalService],
  bootstrap: [AppComponent]  
})
export class AppModule { }
