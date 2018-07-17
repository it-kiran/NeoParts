import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerComponent } from './customer.component';
import { SignupComponent } from './signup/signup.component';
import { SharedModule } from '../shared/shared.module';
import { CustomerService } from './customer.service';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule

  ],
  declarations: [CustomerComponent, SignupComponent, ForgotPasswordComponent, ResetPasswordComponent, ChangePasswordComponent],
  providers: [CustomerService],

})
export class CustomerModule { }
