import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.resetPasswordForm = this.formBuilder.group(
      {
        'password': ['', Validators.required],
        'confirmPassword': ['', Validators.required]
      });
  }

  sendResetLink(){
    
  }
}
