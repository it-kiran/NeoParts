import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { CustomerService } from '../customer.service';
import { Customer } from '../customer.component';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  email: string;
  token:string;

  constructor(private formBuilder: FormBuilder, private customerService: CustomerService, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {

    this.resetPasswordForm = this.formBuilder.group(
      {
        'password': ['', Validators.required],
        'confirmPassword': ['', Validators.required]
      });

      // Here I am getting email address and token from the form the url
      this.email = this.activatedRoute.snapshot.queryParams["email"];
      this.token = this.activatedRoute.snapshot.queryParams["token"];
  }

  resetPassword(){

    let customerDao = new Customer();
    customerDao.email = this.email;
    customerDao.password = this.resetPasswordForm.get('password').value;
    customerDao.confirmPassword = this.resetPasswordForm.get('confirmPassword').value;
    customerDao.token = this.token;

   this.customerService.resetPassword(customerDao);
    
  }
}
