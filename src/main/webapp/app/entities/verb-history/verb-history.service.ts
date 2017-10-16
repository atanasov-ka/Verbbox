import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { VerbHistory } from './verb-history.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VerbHistoryService {

    private resourceUrl = SERVER_API_URL + 'api/verb-histories';

    constructor(private http: Http) { }

    create(verbHistory: VerbHistory): Observable<VerbHistory> {
        const copy = this.convert(verbHistory);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(verbHistory: VerbHistory): Observable<VerbHistory> {
        const copy = this.convert(verbHistory);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<VerbHistory> {
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
     * Convert a returned JSON object to VerbHistory.
     */
    private convertItemFromServer(json: any): VerbHistory {
        const entity: VerbHistory = Object.assign(new VerbHistory(), json);
        return entity;
    }

    /**
     * Convert a VerbHistory to a JSON which can be sent to the server.
     */
    private convert(verbHistory: VerbHistory): VerbHistory {
        const copy: VerbHistory = Object.assign({}, verbHistory);
        return copy;
    }
}
