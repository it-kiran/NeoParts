import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  customerForm: FormGroup;
  loginForm: FormGroup;
  state:string[] = [];

  constructor(private formBuilder: FormBuilder, private customerService: CustomerService) { }

  ngOnInit() {
    
    this.customerForm = this.formBuilder.group(
      {
        'firstName': [null, Validators.required],
        'lastName': [null, Validators.required],
        'phoneNo': ['', [Validators.required, Validators.pattern('^[0-9]+$')]], //TODO - Need to fix this for phono no.
        'email': ['',Validators.required], // TODO - Need to fox this too .com is not validating
        'street': [null, Validators.required],
        'zipCode': ['',Validators.required],
        'city': ['',Validators.required],
        'state': ['Alabama',Validators.required],
        'country':[''],
        'companyName': ['',Validators.required]
      }
    );

    this.loginForm = this.formBuilder.group(
      {
        'email': ['', Validators.required], // TODO - Need to fox this too .com is not validating
        'password': [null, Validators.required]

      }
    );

    this.state = ['Alabama', 'Alaska','Arizona','Arkansas','California','Colorado','Connecticut','Delaware','District Of Columbia','Florida','Georgia','Hawaii','Idaho','Illinois','Indiana','Iowa','Kansas','Kentucky','Louisiana','Maine','Maryland','Massachusetts','Michigan','Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada','New Hampshire','New Jersey','New Mexico','New York','North Carolina','North Dakota','Ohio','Oklahoma','Oregon','Pennsylvania','Rhode Island','South Carolina','South Dakota','Texas','Utah','Vermont','Virginia','Washington','West Virginia','Wisconsin','Wyoming'];
    this.customerForm.get('state').setValue('Alabama');
  }

  login(){

    console.log('inside login');
  }

  register(){

    this.customerService.addOrUpdateCustomer(this.customerForm.value);
    this.customerForm.reset();
    console.log('inside register');

  }
}
