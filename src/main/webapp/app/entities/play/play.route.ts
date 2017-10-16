import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PlayComponent } from './play.component';
import { PlayDetailComponent } from './play-detail.component';
import { PlayPopupComponent } from './play-dialog.component';
import { PlayDeletePopupComponent } from './play-delete-dialog.component';

export const playRoute: Routes = [
    {
        path: 'play',
        component: PlayComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.play.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'play/:id',
        component: PlayDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.play.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const playPopupRoute: Routes = [
    {
        path: 'play-new',
        component: PlayPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.play.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'play/:id/edit',
        component: PlayPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.play.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'play/:id/delete',
        component: PlayDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.play.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
