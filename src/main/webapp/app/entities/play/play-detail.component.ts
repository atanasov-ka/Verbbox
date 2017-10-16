import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Play } from './play.model';
import { PlayService } from './play.service';

@Component({
    selector: 'jhi-play-detail',
    templateUrl: './play-detail.component.html'
})
export class PlayDetailComponent implements OnInit, OnDestroy {

    play: Play;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private playService: PlayService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPlays();
    }

    load(id) {
        this.playService.find(id).subscribe((play) => {
            this.play = play;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPlays() {
        this.eventSubscriber = this.eventManager.subscribe(
            'playListModification',
            (response) => this.load(this.play.id)
        );
    }
}
