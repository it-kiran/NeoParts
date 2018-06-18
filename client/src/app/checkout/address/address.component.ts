import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { GlobalService } from '../../global-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from '../../customer/customer.component';
import { Product } from '../../product-page/product-page.component';
import { ServicesService } from '../../shared/services.service';

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit {
  loginedCustomer: Customer;
  customerForm: FormGroup;
  state:string[] = [];

  purchasedProductList: Product[] = [];
  totalAmount: number = 0.00;
  totalQuantity: number = 0;

  _subscription: any;
  _totalAmountSubscription: any;
  _totalQuantitySubscription: any;

  constructor(private globalService: GlobalService,private formBuilder: FormBuilder, private route: ActivatedRoute, private router: Router, private persistService: ServicesService) {
    this.globalService.getPurchasedProductList();
    this.getPurchasedProductList();

   }

  ngOnInit() {

    this.getCustomerDetails();
    this.globalService.getPurchasedProductList();
    this.getPurchasedProductList();

    
    this.customerForm = this.formBuilder.group(
      {
        'firstName': [this.loginedCustomer.name, Validators.required],
        'lastName': [this.loginedCustomer.name, Validators.required],
        'phoneNo': [this.loginedCustomer.phoneNo, [Validators.required, Validators.pattern('^[0-9]+$')]], //TODO - Need to fix this for phono no.
        'email': [this.loginedCustomer.email,Validators.required], // TODO - Need to fox this too .com is not validating
        'street': [this.loginedCustomer.street, Validators.required],
        'zipCode': [this.loginedCustomer.zipCode,Validators.required],
        'city': [this.loginedCustomer.city,Validators.required],
        'state': [this.loginedCustomer.state,Validators.required],
        'country':[''],
        'companyName': [this.loginedCustomer.companyName,Validators.required],
        'name': [this.loginedCustomer.name],
        'createdTimestamp': ['']
      }
    );

    this.state = ['Alabama', 'Alaska','Arizona','Arkansas','California','Colorado','Connecticut','Delaware','District Of Columbia','Florida','Georgia','Hawaii','Idaho','Illinois','Indiana','Iowa','Kansas','Kentucky','Louisiana','Maine','Maryland','Massachusetts','Michigan','Minnesota','Mississippi','Missouri','Montana','Nebraska','Nevada','New Hampshire','New Jersey','New Mexico','New York','North Carolina','North Dakota','Ohio','Oklahoma','Oregon','Pennsylvania','Rhode Island','South Carolina','South Dakota','Texas','Utah','Vermont','Virginia','Washington','West Virginia','Wisconsin','Wyoming'];

  }

  getPurchasedProductList(){

    this.globalService.getPurchasedProductList();

    this._subscription =  this.globalService.purchasedProductListChange.subscribe((products)=>{
      this.purchasedProductList = products;
      console.log('Product on change by subject From Cart page', this.purchasedProductList);
    });
    this._totalAmountSubscription = this.globalService.totalPurchasedProductAmountChange.subscribe((totalAmount)=>{
      this.totalAmount = totalAmount;
      console.log('subject Total Amount', this.totalAmount);
    });
    this._totalQuantitySubscription = this.globalService.totalPurchasedProductCountChange.subscribe((count)=>{
      this.totalQuantity = count;
      console.log('subject count', this.totalQuantity);
    });

  }

  getCustomerDetails(){
    this.loginedCustomer = this.persistService.getCustomerDetailsForSale();
    console.log('Logined Customer', this.loginedCustomer);
  
  }
  goShippingPage(){
   this.router.navigate(['/shipping']);
  }
  goToViewCartPage(){
    this.router.navigate(['/viewCart']);
  }

  ngOnDestroy() {
    //prevent memory leak when component destroyed
     this._subscription.unsubscribe();
     this._totalAmountSubscription.unsubscribe();
     this._totalQuantitySubscription.unsubscribe();
  }



}
