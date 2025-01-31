import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ApiService } from './api.service';

describe('ApiService', () => {
  let service: ApiService;
  let httpClientMock: jest.Mocked<HttpClient>;
  let url = 'http://localhost:8080/api/v1';

  beforeEach(() => {
    httpClientMock = {
      get: jest.fn(),
      post: jest.fn(),
    } as any;

    TestBed.configureTestingModule({
      providers: [
        ApiService,
        { provide: HttpClient, useValue: httpClientMock },
      ],
    });

    service = TestBed.inject(ApiService);
  });

  describe('get', () => {
    it('should call HttpClient.get with correct URL and return data', () => {
      const mockResponse = { data: 'test' };
      const endpoint = 'test-endpoint';
      httpClientMock.get.mockReturnValue(of(mockResponse));

      service.get(endpoint).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      expect(httpClientMock.get).toHaveBeenCalledWith(`${url}/${endpoint}`);
      expect(httpClientMock.get).toHaveBeenCalledTimes(1);
    });
  });

  describe('post', () => {
    it('should call HttpClient.post with correct URL and data', () => {
      const mockResponse = { success: true };
      const endpoint = 'test-endpoint';
      const postData = { key: 'value' };
      httpClientMock.post.mockReturnValue(of(mockResponse));

      service.post(endpoint, postData).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      expect(httpClientMock.post).toHaveBeenCalledWith(
        `${url}/${endpoint}`,
        postData,
      );
      expect(httpClientMock.post).toHaveBeenCalledTimes(1);
    });
  });
});
