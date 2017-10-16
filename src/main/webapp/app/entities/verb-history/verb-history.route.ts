import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { VerbHistoryComponent } from './verb-history.component';
import { VerbHistoryDetailComponent } from './verb-history-detail.component';
import { VerbHistoryPopupComponent } from './verb-history-dialog.component';
import { VerbHistoryDeletePopupComponent } from './verb-history-delete-dialog.component';

export const verbHistoryRoute: Routes = [
    {
        path: 'verb-history',
        component: VerbHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.verbHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'verb-history/:id',
        component: VerbHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.verbHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const verbHistoryPopupRoute: Routes = [
    {
        path: 'verb-history-new',
        component: VerbHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.verbHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'verb-history/:id/edit',
        component: VerbHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.verbHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'verb-history/:id/delete',
        component: VerbHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.verbHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
