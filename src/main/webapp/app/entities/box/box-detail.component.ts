import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Box } from './box.model';
import { BoxService } from './box.service';

@Component({
    selector: 'jhi-box-detail',
    templateUrl: './box-detail.component.html'
})
export class BoxDetailComponent implements OnInit, OnDestroy {

    box: Box;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private boxService: BoxService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBoxes();
    }

    load(id) {
        this.boxService.find(id).subscribe((box) => {
            this.box = box;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBoxes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'boxListModification',
            (response) => this.load(this.box.id)
        );
    }
}
