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
    ClickOutsideModule
  ],
  declarations: [AppComponent],
  providers: [BackendService, GlobalService],
  bootstrap: [AppComponent]  
})
export class AppModule { }
