import {Injectable } from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms/forms';
import { Customer } from './customer.component';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';
import { ServicesService } from '../shared/services.service';



@Injectable()
export class CustomerService {
private url: string;
private customerDetails: any;
 

 

constructor(private http: Http,  private router: Router, private persitService: ServicesService) { 
 this.url = environment.productUrl;
  //this.getCustomerDetails();
  
}
    addOrUpdateCustomer(customer: Customer)
    {
      this.http.post(this.url+'/addCustomer', customer)
      .subscribe(data => {
        if(data.status == 200 || data.status == 201){

          alert('Customer Added Successfully');
          console.log("registered customer successfully!!!")
        }
      },
        error => {
          console.log(JSON.stringify(error.json()));
    });
    }

    getCustomerDetailsByEmail(email:string){
      let headers = new Headers({
        'Authorization': 'Bearer ' +this.getToken()
        });

      this.http.get(this.url+'/getCustomerDetailsByEmail?email='+email,{headers: headers})
      .subscribe((response:Response)=>{
        if(response){
          console.log('response', response.json());
          this.persitService.setCustomerDetailsForSale(response.json());
          // localStorage.setItem('customerDetails', JSON.stringify(response.json()));

          //this.setCustomerDetailsForSale(response.json());
        }
        // let body = response.json();
        // console.log('body', body);
      })

    }

    

    login(username:string, password:string): Observable<boolean>{
      let headers = new Headers({'Content-Type': 'application/json'});
      return this.http.post(this.url+'/auth',JSON.stringify({username: username, password: password}),{headers: headers})
      .map((response: Response) => {
            // login successful if there's a jwt token in the response
            let token = response.json() && response.json().token;

            if (token) {
              // store username and jwt token in local storage to keep user logged in between page refreshes
              localStorage.setItem('currentUser', JSON.stringify({ username: username, token: token }));
              // return true to indicate successful login

              // Now get complete customer details and stote in local
              this.getCustomerDetailsByEmail(username);
              return true;
          } else {
              // return false to indicate failed login
              return false;
          }
      })
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
    }

    public getToken(): String {
      var currentUser = JSON.parse(localStorage.getItem('currentUser'));

      var token = currentUser && currentUser.token;

      console.log('token from service', token);
      return token ? token : "";
    }

    public getLogedInUser(){
      var currentUser = JSON.parse(localStorage.getItem('currentUser'));
      var user = currentUser && currentUser.username;
      return user ? user : "";
    }
    logout(): void {
      // clear token remove user from local storage to log user out
      localStorage.removeItem('currentUser');
      //this.clearCustomer();
      this.router.navigate(['']);
      window.location.reload();
  }

    private extractData(res: Response): Customer[] {
    let body = res.json();
    // console.log(body);
    return body || {};
  }

    private handleError(error: Response | any) {
    // In a real world app, you might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}