import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerComponent } from './customer.component';
import { SignupComponent } from './signup/signup.component';
import { SharedModule } from '../shared/shared.module';
import { CustomerService } from './customer.service';

@NgModule({
  imports: [
    CommonModule,
    SharedModule

  ],
  declarations: [CustomerComponent, SignupComponent],
  providers: [CustomerService],

})
export class CustomerModule { }
