import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Product } from '../product-page/product-page.component';
import { Http, Response, Headers } from '@angular/http';
import { CustomerService } from '../customer/customer.service';
import { Router } from '@angular/router';
import { GlobalService } from '../global-service.service';
import { ReplaySubject, Subject } from 'rxjs';

@Injectable()
export class CheckoutService {
  private url: string;

  constructor(private http: Http,private customerService: CustomerService, private router: Router,private globalServe: GlobalService) {
    this.url = environment.productUrl;
   }

   placeFinalOrder(lineItemDetails: Product[]){

    let headers = new Headers({
      'Authorization': 'Bearer ' + this.customerService.getToken()
    });

    this.http.post(this.url + '/addTransaction', lineItemDetails, { headers: headers })
    .subscribe((test)=>{
      if(test){
        this.router.navigate(['/thankyou']);
        // TODO this is temp fix, cuase right now i dont know how to clear behaviour subjects.
        window.location.reload();
      }
      console.log('response after transaction',test);
    })
   }

}
