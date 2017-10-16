import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BoxComponent } from './box.component';
import { BoxDetailComponent } from './box-detail.component';
import { BoxPopupComponent } from './box-dialog.component';
import { BoxDeletePopupComponent } from './box-delete-dialog.component';

export const boxRoute: Routes = [
    {
        path: 'box',
        component: BoxComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.box.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'box/:id',
        component: BoxDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.box.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const boxPopupRoute: Routes = [
    {
        path: 'box-new',
        component: BoxPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.box.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'box/:id/edit',
        component: BoxPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.box.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'box/:id/delete',
        component: BoxDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'verbboxApp.box.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
