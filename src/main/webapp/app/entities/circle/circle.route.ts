import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CircleComponent } from './circle.component';
import { CircleDetailComponent } from './circle-detail.component';
import { CirclePopupComponent } from './circle-dialog.component';
import { CircleDeletePopupComponent } from './circle-delete-dialog.component';

export const circleRoute: Routes = [
    {
        path: 'circle',
        component: CircleComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.circle.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'circle/:id',
        component: CircleDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.circle.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const circlePopupRoute: Routes = [
    {
        path: 'circle-new',
        component: CirclePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.circle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'circle/:id/edit',
        component: CirclePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.circle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'circle/:id/delete',
        component: CircleDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.circle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
