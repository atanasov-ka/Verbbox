import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Play } from './play.model';
import { PlayService } from './play.service';

@Injectable()
export class PlayPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private playService: PlayService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.playService.find(id).subscribe((play) => {
                    if (play.lastActiviry) {
                        play.lastActiviry = {
                            year: play.lastActiviry.getFullYear(),
                            month: play.lastActiviry.getMonth() + 1,
                            day: play.lastActiviry.getDate()
                        };
                    }
                    this.ngbModalRef = this.playModalRef(component, play);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.playModalRef(component, new Play());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    playModalRef(component: Component, play: Play): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.play = play;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
