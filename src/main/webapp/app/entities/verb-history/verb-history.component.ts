import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { VerbHistory } from './verb-history.model';
import { VerbHistoryService } from './verb-history.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-verb-history',
    templateUrl: './verb-history.component.html'
})
export class VerbHistoryComponent implements OnInit, OnDestroy {
verbHistories: VerbHistory[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private verbHistoryService: VerbHistoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.verbHistoryService.query().subscribe(
            (res: ResponseWrapper) => {
                this.verbHistories = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInVerbHistories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: VerbHistory) {
        return item.id;
    }
    registerChangeInVerbHistories() {
        this.eventSubscriber = this.eventManager.subscribe('verbHistoryListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
