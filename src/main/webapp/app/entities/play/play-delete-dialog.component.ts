import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Play } from './play.model';
import { PlayPopupService } from './play-popup.service';
import { PlayService } from './play.service';

@Component({
    selector: 'jhi-play-delete-dialog',
    templateUrl: './play-delete-dialog.component.html'
})
export class PlayDeleteDialogComponent {

    play: Play;

    constructor(
        private playService: PlayService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.playService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'playListModification',
                content: 'Deleted an play'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-play-delete-popup',
    template: ''
})
export class PlayDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private playPopupService: PlayPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.playPopupService
                .open(PlayDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
