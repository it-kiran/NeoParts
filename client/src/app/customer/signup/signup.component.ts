import { Component, OnInit, ViewContainerRef } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CustomerService } from '../customer.service';
import * as moment from 'moment';
import { Customer } from '../customer.component';
import { GlobalService } from '../../global-service.service';
import { Router } from '@angular/router';
import { ServicesService } from '../../shared/services.service';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  customerForm: FormGroup;
  loginForm: FormGroup;
  state:string[] = [];
  loginSuccess: boolean = true;


  constructor(private formBuilder: FormBuilder, private customerService: CustomerService, private globalService: GlobalService, private router: Router, private percistance: ServicesService, private toastr: ToastsManager) { 

  }

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
        'companyName': ['',Validators.required],
        'name': [''],
        'createdTimestamp': [''],
        'tier':[''],
        'operationType':[''],
        'taxId':['']
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
    let username = this.loginForm.get('email').value;
    let password = this.loginForm.get('password').value;

    this.customerService.login(username, password)
    .subscribe((response)=>{

      console.log('response', response);
      if(response){
        this.toastr.success('Wel-Come, Login Successfully!!', null, {positionClass: "toast-top-center"});
        this.router.navigate(['']);
        this.globalService.getPurchasedProductList();
       // window.location.reload();
      }else {
        this.loginSuccess = false;
        this.toastr.error('Wrong Username Or Password', null, {positionClass: "toast-top-center"});
      }
    }
    ,
      (err) => {
        if(err == 'Unauthorized'){
          this.loginSuccess = false;
        }
      }
  );
}

  register(){

    if(!this.customerForm.valid){
      alert('Please fill all required fields');
    }

    let name:string = this.customerForm.get('firstName').value;
    let a = this.customerForm.get('lastName');

    this.customerForm.get('name').setValue(this.customerForm.get('firstName').value + ' '+this.customerForm.get('lastName').value);
    this.customerForm.get('createdTimestamp').setValue(moment(Date.now()).format('YYYY-MM-DD HH:mm:ss'));
    this.customerForm.get('tier').setValue(3);
    this.customerForm.get('operationType').setValue('Add');
  
    this.customerService.addOrUpdateCustomer(this.customerForm.value)
    .subscribe(data => {
      if(data.status == 200 || data.status == 201){
        this.toastr.success('Wel-Come'+name+' Please check your email for login details', 'Registered Successfully!!');
        this.router.navigate(['']);
      }
    },
      error => {
        console.log(JSON.stringify(error.json()));
  });
    this.customerForm.reset();

  }
}
