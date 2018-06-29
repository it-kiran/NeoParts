import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { FilterPipe } from '../filter.pipe';
import {MatCardModule, MatButtonModule} from  "@angular/material";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {ToastModule} from 'ng2-toastr/ng2-toastr';
import { ShippingPageComponent } from './shipping-page/shipping-page.component';


@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    RouterModule

  ],
  declarations: [HeaderComponent, FooterComponent,FilterPipe, ShippingPageComponent],
  providers:[],
  bootstrap: [HeaderComponent,FooterComponent],
  exports: [
    ReactiveFormsModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    HeaderComponent, 
    FooterComponent,
    ShippingPageComponent,
    RouterModule
  ],

})
export class SharedModule { }