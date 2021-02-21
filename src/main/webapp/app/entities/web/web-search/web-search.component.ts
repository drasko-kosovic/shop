import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-web-search',
  templateUrl: './web-search.component.html',
  styleUrls: ['./web-search.component.scss'],
})
export class WebSearchComponent implements OnInit {
  constructor(protected router: Router) {}

  ngOnInit(): void {}

  doSearch(value: string): void {
    // console.log(`value=${value}`);
    this.router.navigateByUrl(`web/search/${value}`);
  }
}
