import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  
  forgotPasswordForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private customerService: CustomerService) { }

  ngOnInit() {


    this.forgotPasswordForm = this.formBuilder.group(
      {
        'email': ['', Validators.required]
      });
  }

  sendResetLink(){
    console.log('in side rest')
    this.customerService.sendResetPasswordLink(this.forgotPasswordForm.get('email').value);
    
  }

}
