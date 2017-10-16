import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Verb } from './verb.model';
import { VerbService } from './verb.service';

@Component({
    selector: 'jhi-verb-detail',
    templateUrl: './verb-detail.component.html'
})
export class VerbDetailComponent implements OnInit, OnDestroy {

    verb: Verb;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private verbService: VerbService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVerbs();
    }

    load(id) {
        this.verbService.find(id).subscribe((verb) => {
            this.verb = verb;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVerbs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'verbListModification',
            (response) => this.load(this.verb.id)
        );
    }
}
