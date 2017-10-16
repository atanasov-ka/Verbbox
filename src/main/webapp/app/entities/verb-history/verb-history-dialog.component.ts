import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { VerbHistory } from './verb-history.model';
import { VerbHistoryPopupService } from './verb-history-popup.service';
import { VerbHistoryService } from './verb-history.service';
import { Play, PlayService } from '../play';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-verb-history-dialog',
    templateUrl: './verb-history-dialog.component.html'
})
export class VerbHistoryDialogComponent implements OnInit {

    verbHistory: VerbHistory;
    isSaving: boolean;

    plays: Play[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private verbHistoryService: VerbHistoryService,
        private playService: PlayService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.playService.query()
            .subscribe((res: ResponseWrapper) => { this.plays = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.verbHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.verbHistoryService.update(this.verbHistory));
        } else {
            this.subscribeToSaveResponse(
                this.verbHistoryService.create(this.verbHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<VerbHistory>) {
        result.subscribe((res: VerbHistory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: VerbHistory) {
        this.eventManager.broadcast({ name: 'verbHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPlayById(index: number, item: Play) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-verb-history-popup',
    template: ''
})
export class VerbHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private verbHistoryPopupService: VerbHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.verbHistoryPopupService
                    .open(VerbHistoryDialogComponent as Component, params['id']);
            } else {
                this.verbHistoryPopupService
                    .open(VerbHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
