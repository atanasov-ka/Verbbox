import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Play } from './play.model';
import { PlayPopupService } from './play-popup.service';
import { PlayService } from './play.service';
import { User, UserService } from '../../shared';
import { Box, BoxService } from '../box';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-play-dialog',
    templateUrl: './play-dialog.component.html'
})
export class PlayDialogComponent implements OnInit {

    play: Play;
    isSaving: boolean;

    users: User[];

    boxes: Box[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private playService: PlayService,
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
        if (this.play.id !== undefined) {
            this.subscribeToSaveResponse(
                this.playService.update(this.play));
        } else {
            this.subscribeToSaveResponse(
                this.playService.create(this.play));
        }
    }

    private subscribeToSaveResponse(result: Observable<Play>) {
        result.subscribe((res: Play) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Play) {
        this.eventManager.broadcast({ name: 'playListModification', content: 'OK'});
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
    selector: 'jhi-play-popup',
    template: ''
})
export class PlayPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private playPopupService: PlayPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.playPopupService
                    .open(PlayDialogComponent as Component, params['id']);
            } else {
                this.playPopupService
                    .open(PlayDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
