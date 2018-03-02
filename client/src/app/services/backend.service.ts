import { Injectable } from '@angular/core';
import {  Http,Response,Headers} from '@angular/http';

@Injectable()
export class BackendService {

  private apiURL = 'http://localhost:8080/getWebMenu';
  private searchURL = 'http://localhost:8080/getAllProductForSearch';

  data:any = {};


  appleInfo:any=[];

  constructor(private http:Http) { }

  getData(){
    return this.http.get(this.apiURL, { headers: new Headers({ 'Content-Type': 'application/json' }) })
    .map((res:Response)=>res.json());
  }

  getFilterSearch(value){
    return this.http.get(this.searchURL, { headers: new Headers({ 'Content-Type': 'application/json' }) })
    .map((res:Response)=>res.json());
  }

}
