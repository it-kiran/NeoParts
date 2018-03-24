import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.css']
})
export class CustomerComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }


}

export class Customer {
  phoneNo: string;
  name: string;
  companyName: string;
  email: any;
  // taxId: any;
  // dateOfBirth: any;
  // type: any;
  // gender: any;
  street: any;
  city: string;
  state: string;
  country: string;
  zipCode: number;
  // storeCredit: any;
  // balance: number;
  // lastUpdatedStoreCreditDate: any;
  password: any;
  createdTimestamp: any;
  // customerNote: any;
}
