import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';

import { AppRoutingModule } from './app-routing.module';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import {BackendService} from './services/backend.service';
import { HomeComponent } from './components/home/home.component';
import { SellComponent } from './components/sell/sell.component';

import { RouterModule, Routes } from '@angular/router';

import { FilterPipe} from './filter.pipe';
import { ClickOutsideModule } from 'ng4-click-outside';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SellComponent,
    FilterPipe 
   
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule,
    ClickOutsideModule
  ],
  providers: [BackendService],
  bootstrap: [AppComponent]
  
})
export class AppModule { }
