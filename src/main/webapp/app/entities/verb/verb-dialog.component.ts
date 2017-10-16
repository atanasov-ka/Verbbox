import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Verb } from './verb.model';
import { VerbPopupService } from './verb-popup.service';
import { VerbService } from './verb.service';
import { Box, BoxService } from '../box';
import { VerbHistory, VerbHistoryService } from '../verb-history';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-verb-dialog',
    templateUrl: './verb-dialog.component.html'
})
export class VerbDialogComponent implements OnInit {

    verb: Verb;
    isSaving: boolean;

    boxes: Box[];

    verbhistories: VerbHistory[];
    createdDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private verbService: VerbService,
        private boxService: BoxService,
        private verbHistoryService: VerbHistoryService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.boxService.query()
            .subscribe((res: ResponseWrapper) => { this.boxes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.verbHistoryService
            .query({filter: 'verb-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.verb.verbHistory || !this.verb.verbHistory.id) {
                    this.verbhistories = res.json;
                } else {
                    this.verbHistoryService
                        .find(this.verb.verbHistory.id)
                        .subscribe((subRes: VerbHistory) => {
                            this.verbhistories = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.verb.id !== undefined) {
            this.subscribeToSaveResponse(
                this.verbService.update(this.verb));
        } else {
            this.subscribeToSaveResponse(
                this.verbService.create(this.verb));
        }
    }

    private subscribeToSaveResponse(result: Observable<Verb>) {
        result.subscribe((res: Verb) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Verb) {
        this.eventManager.broadcast({ name: 'verbListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackBoxById(index: number, item: Box) {
        return item.id;
    }

    trackVerbHistoryById(index: number, item: VerbHistory) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-verb-popup',
    template: ''
})
export class VerbPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private verbPopupService: VerbPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.verbPopupService
                    .open(VerbDialogComponent as Component, params['id']);
            } else {
                this.verbPopupService
                    .open(VerbDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
