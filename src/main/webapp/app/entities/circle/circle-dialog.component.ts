import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Circle } from './circle.model';
import { CirclePopupService } from './circle-popup.service';
import { CircleService } from './circle.service';
import { User, UserService } from '../../shared';
import { Box, BoxService } from '../box';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-circle-dialog',
    templateUrl: './circle-dialog.component.html'
})
export class CircleDialogComponent implements OnInit {

    circle: Circle;
    isSaving: boolean;

    users: User[];

    boxes: Box[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private circleService: CircleService,
        private userService: UserService,
        private boxService: BoxService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.boxService.query()
            .subscribe((res: ResponseWrapper) => { this.boxes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.circle.id !== undefined) {
            this.subscribeToSaveResponse(
                this.circleService.update(this.circle));
        } else {
            this.subscribeToSaveResponse(
                this.circleService.create(this.circle));
        }
    }

    private subscribeToSaveResponse(result: Observable<Circle>) {
        result.subscribe((res: Circle) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Circle) {
        this.eventManager.broadcast({ name: 'circleListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackBoxById(index: number, item: Box) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-circle-popup',
    template: ''
})
export class CirclePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private circlePopupService: CirclePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.circlePopupService
                    .open(CircleDialogComponent as Component, params['id']);
            } else {
                this.circlePopupService
                    .open(CircleDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
