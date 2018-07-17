import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit() {

    this.changePasswordForm = this.formBuilder.group(
      {
        'oldPassword':['', Validators.required],
        'password': ['', Validators.required],
        'confirmPassword': ['', Validators.required]
      });
  }

  sendResetLink(){
    
  }

}
