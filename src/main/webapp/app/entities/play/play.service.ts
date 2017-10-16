import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Play } from './play.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PlayService {

    private resourceUrl = SERVER_API_URL + 'api/plays';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(play: Play): Observable<Play> {
        const copy = this.convert(play);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(play: Play): Observable<Play> {
        const copy = this.convert(play);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Play> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Play.
     */
    private convertItemFromServer(json: any): Play {
        const entity: Play = Object.assign(new Play(), json);
        entity.lastActiviry = this.dateUtils
            .convertDateTimeFromServer(json.lastActiviry);
        return entity;
    }

    /**
     * Convert a Play to a JSON which can be sent to the server.
     */
    private convert(play: Play): Play {
        const copy: Play = Object.assign({}, play);

        copy.lastActiviry = this.dateUtils.toDate(play.lastActiviry);
        return copy;
    }
}
