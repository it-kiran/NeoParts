import { Component, OnInit } from '@angular/core';

import { Http,Response } from '@angular/http';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import {BackendService} from '../../services/backend.service';

@Component({
  selector: 'app-sell',
  templateUrl: './sell.component.html',
  styleUrls: ['./sell.component.css']
})
export class SellComponent implements OnInit {

  constructor(private http:Http,private backendService:BackendService,private router: Router) { }

  ngOnInit() {
      console.log(this.backendService.appleInfo);
  }

}
