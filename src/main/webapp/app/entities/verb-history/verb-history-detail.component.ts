import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { VerbHistory } from './verb-history.model';
import { VerbHistoryService } from './verb-history.service';

@Component({
    selector: 'jhi-verb-history-detail',
    templateUrl: './verb-history-detail.component.html'
})
export class VerbHistoryDetailComponent implements OnInit, OnDestroy {

    verbHistory: VerbHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private verbHistoryService: VerbHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVerbHistories();
    }

    load(id) {
        this.verbHistoryService.find(id).subscribe((verbHistory) => {
            this.verbHistory = verbHistory;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVerbHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'verbHistoryListModification',
            (response) => this.load(this.verbHistory.id)
        );
    }
}
