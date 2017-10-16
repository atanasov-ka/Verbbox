import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { VerbHistory } from './verb-history.model';
import { VerbHistoryPopupService } from './verb-history-popup.service';
import { VerbHistoryService } from './verb-history.service';

@Component({
    selector: 'jhi-verb-history-delete-dialog',
    templateUrl: './verb-history-delete-dialog.component.html'
})
export class VerbHistoryDeleteDialogComponent {

    verbHistory: VerbHistory;

    constructor(
        private verbHistoryService: VerbHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.verbHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'verbHistoryListModification',
                content: 'Deleted an verbHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-verb-history-delete-popup',
    template: ''
})
export class VerbHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private verbHistoryPopupService: VerbHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.verbHistoryPopupService
                .open(VerbHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
