import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Box } from './box.model';
import { BoxPopupService } from './box-popup.service';
import { BoxService } from './box.service';
import { Circle, CircleService } from '../circle';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-box-dialog',
    templateUrl: './box-dialog.component.html'
})
export class BoxDialogComponent implements OnInit {

    box: Box;
    isSaving: boolean;

    sharedwiths: Circle[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private boxService: BoxService,
        private circleService: CircleService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.circleService
            .query({filter: 'box(name)-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.box.sharedWith || !this.box.sharedWith.id) {
                    this.sharedwiths = res.json;
                } else {
                    this.circleService
                        .find(this.box.sharedWith.id)
                        .subscribe((subRes: Circle) => {
                            this.sharedwiths = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.box.id !== undefined) {
            this.subscribeToSaveResponse(
                this.boxService.update(this.box));
        } else {
            this.subscribeToSaveResponse(
                this.boxService.create(this.box));
        }
    }

    private subscribeToSaveResponse(result: Observable<Box>) {
        result.subscribe((res: Box) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Box) {
        this.eventManager.broadcast({ name: 'boxListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCircleById(index: number, item: Circle) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-box-popup',
    template: ''
})
export class BoxPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boxPopupService: BoxPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.boxPopupService
                    .open(BoxDialogComponent as Component, params['id']);
            } else {
                this.boxPopupService
                    .open(BoxDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
